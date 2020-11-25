package org.belog.poopalkombat.model.net.client;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import org.apache.log4j.*;

import org.belog.poopalkombat.model.Model;
import org.belog.poopalkombat.model.net.message.FighterParameters;
import org.belog.poopalkombat.model.net.message.Message;
import org.belog.poopalkombat.model.net.message.MessageType;

public class TCPClient implements Runnable {
    // connection
    private Socket socket;
    private String ip;
    private int port;

    // messages
    ObjectInputStream reader;
    ObjectOutputStream writer;
    BufferedReader consoleReader;

    Model model;    // model that client belongs to

    // states
    private boolean isConnected = true;
    boolean bothChose = false;
    boolean ready = false;
    boolean fighting = false;
    boolean isLoser = false;

    // thread
    private int FPS = 60;
    private long targetTime = 500 / FPS;

    // logs
    private static final Logger logger = LogManager.getLogger(TCPClient.class.getName());

    public TCPClient(String ip, int port, Model model) {
        this.ip = ip;
        this.port = port;
        this.model = model;
    }

    public void close() throws IOException {
        if (socket != null) { socket.close(); }
    }

    @Override
    public void run() {
        try {
            // creating socket
            socket = new Socket(ip, port);

            // setting model parameters
            model.setIsOnlineMode(true);    // online
            model.setIsServer(false);    // we are client
            model.setIsConnected(true); // we are connected to server
            model.setScene("PVP_CHOICE_SCENE"); // choosing fighter

            writer = new ObjectOutputStream(socket.getOutputStream());
            reader = new ObjectInputStream(socket.getInputStream());
            consoleReader = new BufferedReader(new InputStreamReader(System.in));

            // time
            long start;
            long elapsed;
            long wait;

            logger.info("CONNECTING...");

            while (isConnected) {
                start = System.currentTimeMillis();

                // sending/receiving messages to/from server
                send();
                receive();

                if (!model.getView().getIsRunning() || !model.isConnected()) {
                    // disconnecting
                    logger.info("DISCONNECTING...");
                    socket.close();
                    isConnected = false;
                }

                if (!model.isWinner() && model.getWinnerID() == 1) {
                    // opponent won
                    isLoser = true;
                }

                if (bothChose && ready && !fighting) {
                    // start fighting
                    logger.info("START FIGHTING...");
                    fighting = true;
                    model.setScene("PVP_FIGHT_SCENE");
                }

                elapsed = System.nanoTime() - start;
                wait = targetTime - elapsed / 1000000;
                if (wait < 0) { wait = 5; }
                Thread.sleep(wait);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void send() {
        try {
            logger.info("SENDING MESSAGE...");

            if (!bothChose && model.getChose()) {
                // we chose, server did not
                logger.info("TYPE == CHOSE, MESSAGE == SERVER_CHOSE");
                writer.writeObject(new Message(MessageType.CHOSE, "CLIENT_CHOSE"));
            } else if (bothChose && !ready) {
                // sending our fighter's name
                logger.info("TYPE == FIGHTER_NAME, MESSAGE == " + model.getFightersNames()[0]);
                writer.writeObject(new Message(MessageType.FIGHTER_NAME, model.getFightersNames()[0]));
            } else if (isLoser) {
                // telling server that we lose
                logger.info("TYPE == LOSE, MESSAGE == CLIENT_LOSE");
                writer.writeObject(new Message(MessageType.LOSE, "CLIENT_LOSE"));
                model.setIsWinner(false);   // client is loser
                model.getView().stopMusic();    // stopping music
                model.setScene("WIN_SCENE");    // setting win-scene
                model.setIsConnected(false);    // disconnecting
                logger.info("END OF FIGHTING...");
            } else if (fighting) {
                // sending our fighter's parameters
                logger.info("TYPE == FIGHTER_PARAMETERS, MESSAGE == FIGHTER_PARAMETERS");
                writer.writeObject(model.getFighter(1).createParametersPack());
            } else {
                // sending empty message
                writer.writeObject(new Message(MessageType.EMPTY, ""));
            }

            writer.flush();
        } catch (Exception e) {
            if (e.getClass() == SocketException.class) {
                // disconnecting

                model.setIsConnected(false);

                try {
                    model.setScene("DISCONNECTION_SCENE");
                } catch (Exception e1) {
                    logger.error(e1.getMessage());
                    e1.printStackTrace();
                }
            }

            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private void receive() {
        try {
            logger.info("WAITING FOR MESSAGE...");
            Message message = (Message) reader.readObject();
            logger.info("RECEIVED MESSAGE: TYPE == " + message.getType() + ", MESSAGE == " + message.getMessage());

            if (message.getType() == MessageType.CHOSE) {
                // server chose
                if (model.getChose()) {
                    // if chose too
                    bothChose = true;   // both chose their fighters
                }
            } else if (message.getType() == MessageType.FIGHTER_NAME) {
                if (!bothChose) {
                    // server thinks that both chose their fighters
                    // let it be
                    bothChose = true;
                }
                // setting received name as second fighter's name
                String[] fightersNames = new String[2];
                fightersNames[1] = model.getFightersNames()[0];
                fightersNames[0] = message.getMessage();
                model.setFightersNames(fightersNames);
                ready = true;    // we are ready to fight
            } else if (message.getType() == MessageType.FIGHTER_PARAMETERS) {
                // receiving server-fighter's parameters
                model.getFighter(0).setParameters((FighterParameters) message);
            } else if (message.getType() == MessageType.LOSE) {
                // client lose
                model.setIsWinner(true);   // server is winner
                model.getView().stopMusic();    // stopping music
                model.setScene("WIN_SCENE");
                model.setIsConnected(false);    // disconnecting
                logger.info("END OF FIGHTING...");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}