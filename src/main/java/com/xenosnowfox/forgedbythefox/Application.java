package com.xenosnowfox.forgedbythefox;

import spark.Spark;

import java.util.logging.Logger;

/** Main Application. */
public class Application implements Runnable {

    private static final Logger LOG = Logger.getLogger(Application.class.getName());

    /**
     * Primary entrypoint from the command line.
     *
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
        // THESE COMMANDS ARE ONLY EXECUTED WHEN RUNNING THE APPLICATION
        // FROM EITHER INSIDE INTELLIJ OR FROM THE COMMAND LINE
        LOG.info("Running Application from command line.");
        final Application application = new Application();
        application.run();
        Spark.init();
    }


    @Override
    public final void run() {
        System.out.println("Setting up Exception Handlers");
        // #TODO: Implement exception handlers

        System.out.println("Setting up Spark routes.");
        // #TODO: Implement Spark routes

        System.out.println("Spark running on port " + Spark.port());
    }

}
