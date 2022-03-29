import io.grpc.Server;
import io.grpc.ServerBuilder;
import services.UserService;

import java.io.IOException;

public class gRPC_Server {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.
                forPort(2634)
                .addService(new UserService())
                .build();

        server.start();
        System.out.println("Server started at "+ server.getPort());

        server.awaitTermination();
    }
}
