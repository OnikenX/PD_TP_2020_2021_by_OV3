package pt.isec.LEI.PD.TP20_21.pdtpbootstrap.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Token
{
    private static final HashMap<String, String> allTokens = new HashMap<>();



    public static String getNewToken(String username)
    {
        SimpleDateFormat sDF = new SimpleDateFormat("dd-MM-yyyy##HH:mm:ss:SSS");
        String curDate = sDF.format(new Date());
        String tokenContent = username + "//" + curDate;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(tokenContent.getBytes(StandardCharsets.UTF_8));
            String token = Base64.getEncoder().encodeToString(hash);
            allTokens.put(username, token);
            return token;
        }catch (NoSuchAlgorithmException e)
        {
            return null;
        }
    }

    public static boolean validateToken(String token)
    {
        return allTokens.containsValue(token);
    }

    public static String getUsernameByToken(String token)
    {
        for (Map.Entry<String, String> e : allTokens.entrySet())
            if (e.getValue().equals(token))
                return e.getKey();
        return null;
    }
}
