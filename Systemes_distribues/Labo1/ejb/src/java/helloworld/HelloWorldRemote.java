package helloworld;
import javax.ejb.Remote;
@Remote
public interface HelloWorldRemote
{
String sayHello();
}

