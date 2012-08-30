package helloworld;
import javax.ejb.Stateless;
@Stateless
public class HelloWorldBean implements HelloWorldRemote
{
public String sayHello()
{
return "hello";
}
}