package suishen.redis.exception;

/**
 * Author: Bryant Hang
 * Date: 15/2/4
 * Time: 下午3:05
 */
/**
 * redis操作过程中的异常
 */
public class RedisRuntimeException extends RuntimeException{

    public RedisRuntimeException(){
        super();
    }

    public RedisRuntimeException(String message){
        super(message);
    }

    public RedisRuntimeException(Throwable cause) {
        super(cause);
    }

    public RedisRuntimeException(String message, Throwable cause){
        super(message, cause);
    }

}
