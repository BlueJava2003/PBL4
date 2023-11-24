package SERVER.HELPER;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.Random;

public class SendEmail {

//    public static void write(Object obj) throws IOException {
//        Constants.oos.writeObject(obj);
//        Constants.oos.flush();
//    }

    public static Message sendMail(String recipient) throws Exception {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        String email = "lenhandeveloper@gmail.com";
        //xhvitnnifhxwdwpw
        String password = "pijpszumoozohkce";
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
        Message message = prepareMessage(session, email, recipient);
        Transport.send(message);
        return message;

    }

    //Tra ve ket qua mess trong mail
    public static String getTextFromMessage(Message message) throws MessagingException, IOException {
        String result = "";
        if (message.isMimeType("text/plain")) {
            result = message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            result = getTextFromMimeMultipart(mimeMultipart);
        }
        return result;
    }

    public static String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart) throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break; // without break same text appears twice in my tests
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
            }
        }
        return result;
    }

    public static Message prepareMessage(Session session, String email, String recipient) throws Exception {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(email));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject("your OTP is...");
        message.setText(getRandomNumberString());
        return message;
    }

    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    public static String encriptMD5(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte[] digest = md.digest();
        String myHash = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return myHash;
    }

    public static boolean verifyPasswordMD5(String inputPassword, String hashPassWord)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(inputPassword.getBytes());
        byte[] digest = md.digest();
        String myChecksum = DatatypeConverter
                .printHexBinary(digest).toUpperCase();
        return hashPassWord.equals(myChecksum);
    }
    public static void main(String args[]) throws Exception{
        sendMail("lenhan0029@gmail.com");
    }
    
}
