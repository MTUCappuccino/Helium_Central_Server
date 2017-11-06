import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CentralJUnit 
{

	private ServerSpec s;
	private CentralServer c;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception 
	{
		System.out.println("start class");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception 
	{
		System.out.println("\nend class");
	}

	@Before
	public void setUp() throws Exception 
	{
		s = new ServerSpec("ServerName","picURL","theURL","port1",true,true,"code1",true);
		System.out.println("\nstart method\n");
	}

	@After
	public void tearDown() throws Exception 
	{
		
		System.out.println("\nend method");
	}

	@Test
	public void testName() 
	{
		System.out.println("testing getName");
		String expectedName = "Server Name";
		String actualName = s.getName();
		if(!(expectedName.equals(expectedName)))
		{
			System.out.println("expected: " + expectedName + " got: " + actualName);
			fail("the getName method was not executed correctly");
		}
		else
			System.out.println("Success");
	}
	
	@Test
	public void testPort() 
	{
		System.out.println("testing getPort");
		String expectedPort = "port1";
		String actualPort = s.getPort();
		if(!(expectedPort.equals(actualPort)))
		{
			System.out.println("expected: " + expectedPort + " got: " + actualPort);
			fail("the getPort method was not executed correctly");
		}
		else
			System.out.println("Success");
	}
	
	@Test
	public void testURL() 
	{
		System.out.println("testing getURL");
		String expectedURL = "theURL";
		String actualURL = s.getURL();
		if(!(expectedURL.equals(expectedURL)))
		{
			System.out.println("expected: " + expectedURL + " got: " + actualURL);
			fail("the getURL method was not executed correctly");
		}
		else
			System.out.println("Success");
	}
	
	@Test
	public void testCode() 
	{
		System.out.println("testing getCode");
		String expectedCode = "code1";
		String actualCode = s.getCode();
		if(!(expectedCode.equals(expectedCode)))
		{
			System.out.println("expected: " + expectedCode + " got: " + actualCode);
			fail("the getCode method was not executed correctly");
		}
		else
			System.out.println("Success");
	}

}
