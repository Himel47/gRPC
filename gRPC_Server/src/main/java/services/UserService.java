package services;

import com.demo.grpc.User;
import com.demo.grpc.userGrpc.userImplBase;
import io.grpc.stub.StreamObserver;

import java.util.logging.Logger;

public class UserService extends userImplBase {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @Override
    public void login(User.LoginReq request, StreamObserver<User.APIres> responseObserver) {

        String username = request.getUsername();
        String password = request.getPassword();
        logger.info("Login from: "+ username);

        User.APIres.Builder response= User.APIres.newBuilder();

        if (username.equals("Himel47") && password.equals("Shahriar44")){
            //return success message
            response.setResCode(0).setMessage("SUCCESS");
            logger.info("Login Successful.");
        }
        else{
            //return failure message
            response.setResCode(100).setMessage("INVALID MESSAGE");
            logger.info("Login Failed.");
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void logout(User.Empty request, StreamObserver<User.APIres> responseObserver) {
        //super.logout(request, responseObserver);
    }
}
