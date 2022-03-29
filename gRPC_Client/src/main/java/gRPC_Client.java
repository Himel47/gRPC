import com.demo.grpc.User;
import com.demo.grpc.userGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.sql.*;
import java.util.logging.Logger;

public class gRPC_Client {

    public static final Logger logger = Logger.getLogger(gRPC_Client.class.getName());

    public static void main(String[] args) {
        try{
            Connection myConnection = DriverManager.getConnection("jdbc:mysql://localhost:2634/gRPC_demo", "username", "password");
            Statement myState = myConnection.createStatement();
            ResultSet myResult = myState.executeQuery("select * from info");
            while(myResult.next()){
                System.out.println(myResult.getString("username") +", "+ myResult.getString("password"));
            }

        }catch(Exception e) {
            e.printStackTrace();
        }

        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost",2634)
                .usePlaintext()
                .build();
        //stubs to call particular grpc-generate from proto

        userGrpc.userBlockingStub userStub = userGrpc.newBlockingStub(channel);
        User.LoginReq request = User.LoginReq.newBuilder()
                .setUsername("Himel47")
                .setPassword("Shahriar44")
                .build();

        User.APIres apiRes = userStub.login(request);
        logger.info(apiRes.getResCode() + "\n" + apiRes.getMessage());
    }
}
