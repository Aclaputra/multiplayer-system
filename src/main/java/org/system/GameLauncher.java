package org.system;

public class GameLauncher {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java -jar <app_name>.jar [server/client]");
            return;
        }

        String mode = args[0].toLowerCase();

        if (mode.equals("server")) {
            System.out.println("starting in server mode...");
            PlayerServer.start();
        } else if (mode.equals("client")) {
            System.out.println("Starting in client mode...");

            Thread nettyThread = new Thread(PlayerClient::start);
            nettyThread.setDaemon(true);
            nettyThread.start();

            Game.start();
        } else if (mode.equals("game")) {
            System.out.println("Starting Game Mode.");
            Game.start();
        } else{
            System.out.println("Unknown code: " + mode);
        }
    }
}
