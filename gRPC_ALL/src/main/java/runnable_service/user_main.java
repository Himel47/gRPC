package runnable_service;

import com.himel.grpc.User;
import com.himel.grpc.userGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Scanner;

public class user_main {
    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",2634)
                .usePlaintext()
                .build();
        userGrpc.userBlockingStub userBlockingStub = new userGrpc.userBlockingStub(managedChannel);

        boolean authorise =false;
        Scanner scanner = new Scanner(System.in);

        System.out.println("New User? Yes/No");
        String choice = scanner.next();
        if(choice.equalsIgnoreCase("Yes")){
            registration(userBlockingStub);
        }

        while(!authorise){
            System.out.println("Enter Name: ");
            String name = scanner.next();
            System.out.println("Enter password: ");
            String pass = scanner.next();
            User.loginRequest loginRequest = User.loginRequest.newBuilder()
                    .setUserName(name)
                    .setPassword(pass)
                    .build();
            User.loginResponse loginResponse = userBlockingStub.login(loginRequest);

            if(loginResponse.getLogResCode()==200){
                authorise = true;
            }
            System.out.println(loginResponse.getLogResponse());
        }
    }

    private static void registration(userGrpc.userBlockingStub userBlockingStub) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your userID: ");
        int ID = scanner.nextInt();
        System.out.println("Enter your Name: ");
        String name =scanner.next();
        System.out.println("Enter username: ");
        String userName = scanner.next();
        System.out.println("Enter password: ");
        String pass = scanner.next();

        User.regRequest regRequest = User.regRequest.newBuilder()
                .setUserID(ID)
                .setName(name)
                .setUserName(userName)
                .setPassword(pass)
                .build();

        User.regResponse regResponse = userBlockingStub.registration(regRequest);
        System.out.println(regResponse.getRegResponse());
    }
}
