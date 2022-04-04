package services;

import com.himel.grpc.User;
import com.himel.grpc.userGrpc;
import com.himel.grpc.userGrpc.userImplBase;
import io.grpc.ServerServiceDefinition;
import io.grpc.stub.StreamObserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.sql.DriverManager.getConnection;

public class userService extends userImplBase {

    String location = "jdbc:mysql://localhost:3306/grpc_demo";
    String user = "root";
    String pass = "";

    @Override
    public void registration(User.regRequest request, StreamObserver<User.regResponse> responseObserver) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        int userID = request.getUserID();
        String name = request.getName();
        String username= request.getUserName();
        String password = request.getPassword();

        ResultSet resultSet = checkRegisterInfo(userID);

        User.regResponse.Builder response = User.regResponse.newBuilder();

        while(resultSet.next()){
            if(resultSet.getInt(1)==1){
                response.setRegResponse(userID + "is Already Registered.").setRegResCode(500);
            }
            else{
                Connection connection = getConnection(location,user,pass);
                PreparedStatement preparedStatement = connection
                        .prepareStatement("INSERT INTO user_info VALUES('"+userID+"','"+username+"','"+password+"')");
                preparedStatement.executeUpdate();
                response.setRegResponse("UserID "+userID+ " named " +username+" is registered Successfully.").setRegResCode(100);
            }
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    private ResultSet checkRegisterInfo(int userID) throws SQLException {
        Connection connection = getConnection(location,user,pass);
        PreparedStatement preparedStatement = connection
                .prepareStatement("SELECT EXISTS(SELECT * FROM user_info WHERE regID = ?)");
        preparedStatement.setInt(1,userID);
        return preparedStatement.executeQuery();
    }

    @Override
    public void login(User.loginRequest request, StreamObserver<User.loginResponse> responseObserver) throws SQLException, ClassNotFoundException {
        String userName = request.getUserName();
        String password = request.getPassword();

        ResultSet resultSet = checkLoginInfo(userName,password);

        User.loginResponse.Builder response = User.loginResponse.newBuilder();

        while(resultSet.next()){
            if(resultSet.getInt(1)==1){
                response.setLogResCode(200).setLogResponse("Successfully Logged in.");
            }
            else{
                response.setLogResponse("Wrong username or password").setLogResCode(400);
            }
            break;
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    private ResultSet checkLoginInfo(String userName, String password) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = getConnection(location,user,pass);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT EXISTS(SELECT * FROM user_info" +
                " WHERE username =  BINARY '"+userName+"' && password = BINARY '"+password+"')");
        return preparedStatement.executeQuery();
    }
}
