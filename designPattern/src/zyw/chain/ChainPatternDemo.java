package zyw.chain;

public class ChainPatternDemo {

    public static void main(String[] args) {

        AbstractLogger errorLogger = new ErrorLogger(AbstractLogger.ERROR);
        AbstractLogger fileLogger = new FileLogger(AbstractLogger.DEBUG);
        AbstractLogger consoleLogger = new ConsoleLogger(AbstractLogger.INFO);


        errorLogger.setNextLogger(fileLogger);
        fileLogger.setNextLogger(consoleLogger);


//        errorLogger.logMessage(AbstractLogger.INFO,
//                "This is an information.");

        errorLogger.logMessage(AbstractLogger.DEBUG,
                "This is a debug level information.");

//        errorLogger.logMessage(AbstractLogger.ERROR,
//                "This is an error information.");
    }

}
