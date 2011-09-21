package helloworld;
import javax.ejb.EJB;

public class Main
{
	@EJB
	private static helloworld.HelloWorldRemote helloWorldRemote;
	public Main()
	{
	}

	public static void main(String[] args)
	{
	try
		{
		System.out.println(helloWorldRemote.sayHello());
		}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	}

}
