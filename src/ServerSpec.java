import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class ServerSpec 
{	
	public ServerSpec(String n, String purl, String u, String p, boolean ru, boolean rp, String c, boolean i)
    {
		setName(n);
		setpURL(purl);
		seturlAddress(u);
		setPort(p);
		setReqUser(ru);
		setReqPass(rp);
		setCode(c);
		setisPub(i);
    }
		
	public void setName(String n)
	{
		name = n;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setpURL(String p)
	{
		pictureURL = p;
	}
	
	public void seturlAddress(String u)
	{
		urlAddress = u;
	}
	
	public String getURL()
	{
		return urlAddress;
	}
	
	public void setPort(String p)
	{
		port = p;
	}
	
	public String getPort()
	{
		return port;
	}
	
	public void setReqUser(boolean ru)
	{
		reqUsername = ru;
	}
	
	public void setReqPass(boolean rp)
	{
		reqPassword = rp;
	}
	
	public void setCode(String c)
	{
		code = c;
	}
	
	public void setisPub(boolean i)
	{
		isPublic = i;
	}
	
	public void storePictureURL(String picture, String dest) throws IOException
	{
		URL url = new URL(picture);
		InputStream iStream = url.openStream();
		OutputStream oStream = new FileOutputStream(dest);
		byte[] a = new byte[2048];
		int size;
		while((size = iStream.read(a)) != -1)
		{
			oStream.write(a,  0, size);
		}
			
		iStream.close();
		oStream.close();
	}
	
    private String name;
    private String pictureURL;
    private String urlAddress;
    private String port;
    private boolean reqUsername;
    private boolean reqPassword;
    private String code;
    private boolean isPublic;
}
