package net.jacktools.barcode.barcodegenerator.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.*;

public class AppLog {
    private static Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(Assets.getString("application.title"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyMMdd_HHmmss_SSS").withLocale(Locale.getDefault());
        try {
            Path logPath = Paths.get(System.getProperty("user.home")).resolve(Assets.getString("application.path.root")).resolve(Assets.getString("application.path.log")).resolve(LocalDateTime.now().format(formatter) + ".log");
            FileHandler fileHandler;
            Files.createDirectories(logPath.getParent());
            fileHandler = new FileHandler(logPath.toString(), true);
            fileHandler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    SimpleDateFormat logTime = new SimpleDateFormat("yyy.MM.dd HH:mm:ss.SSS");
                    Calendar cal = new GregorianCalendar();
                    return record.getLevel() +
                            " - " +
                            logTime.format(cal.getTime()) +
                            " || " +
                            record.getSourceClassName().substring(
                                    record.getSourceClassName().lastIndexOf(".") + 1
                            ) +
                            "." +
                            record.getSourceMethodName() +
                            "() : " +
                            record.getMessage() + "\n";
                }
            });
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.toString());
        }
    }

    /**
     * Write level and message to logfile.
     *
     * @param level the level
     * @param msg   the message
     */
    public static void log(Level level, String msg) {
        LOGGER.log(level, msg);
    }

}
