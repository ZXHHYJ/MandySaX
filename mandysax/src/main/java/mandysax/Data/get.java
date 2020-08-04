package mandysax.Data;
import java.io.*;
import java.net.*;
import org.json.*;

public class get
{
    public static String jsonstring(String p0, String p1)
    {	
        try
        {  if (p0 == null)return null;
            JSONObject jsonObject = new JSONObject(p0);  
            return jsonObject != null ?jsonObject.optString(p1): null;  
        }
        catch (JSONException e)
        {  
            e.printStackTrace();
        }  
		return null;
	}

    public static int jsonint(String p0, String p1)
    {
        try
        {  
            if (p0 == null)return 0;
            JSONObject jsonObject = new JSONObject(p0);  
            return jsonObject.opt(p1);
        }
        catch (JSONException e)
        {  
            e.printStackTrace();  
            return 0;
        }  
    }

    public static double jsondouble(String p0, String p1)
    {
        try
        {  
            JSONObject jsonObject = new JSONObject(p0);  
            return jsonObject.optDouble(p1);
        }
        catch (JSONException e)
        {  
            e.printStackTrace();  
            return 0;
        }  
    }

    public static String getString(String geturl)
    {    
        try
        {   
            URL url = new URL(geturl); 
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();   
            connection.setConnectTimeout(1000);   
            connection.setRequestMethod("GET");   
            int responseCode = connection.getResponseCode();  
            if (responseCode == 200)
            {           
                InputStream is = connection.getInputStream();  
                return inputStream2String(is);  
            }
            else
            {              
                return null;     
            }     
        }
        catch (MalformedURLException e)
        {    
            e.printStackTrace();    
        }
        catch (ProtocolException e)
        {    
            e.printStackTrace(); 
        }
        catch (IOException e)
        {    
            e.printStackTrace();   
        }     
        return null;
    }

    private static String inputStream2String(InputStream is) throws IOException
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        while ((i = is.read()) != -1)
        {
            byteArrayOutputStream.write(i);
        }
        return byteArrayOutputStream.toString();
    }
}

