package runnable_service;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import services.userService;

import java.io.IOException;
import java.util.logging.Logger;

public class gRPC_main {

    static Logger logger = Logger.getLogger(Server.class.getName());
    public static void main(String[] args) throws IOException, InterruptedException {
        io.grpc.Server server = ServerBuilder.forPort(2634)
                .addService(new userService())
                .build();
        server.start();
        logger.info("Server Started at port" + server.getPort());
        server.awaitTermination();
    }
}
