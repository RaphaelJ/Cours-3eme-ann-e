package calculs;

import javax.ejb.EJB;
import java.io.*;

public class Main
{
	@EJB
	private static calculs.CalculsRemote calculsRemote;

	public Main()
	{
	}

//    public static int readInt() throws IOException
  //  {
    //    BufferedReader inStream = new BufferedReader (
      //      new InputStreamReader(System.in)
       // );
        //return Integer.parseInt(inStream.readLine()); 
//    }

	public static void main(String[] args) throws Exception
	{
		for (;;) {
			System.out.println("1. Ajouter un nombre");	
			System.out.println("2. Calculer la somme");	
			System.out.println("3. Calculer la moyenne");	
			System.out.println("4. Calculer le minimum");	
			System.out.println("5. Calculer le maximum");	
			System.out.println("Choix: ");
			int choix = 1; //readInt(); 

			if (choix == 1) {
				System.out.println("Nombre à rajouter: ");
				calculsRemote.add(1); //readInt());
			} else if (choix == 2) {
				System.out.println("Somme: " + calculsRemote.sum());
			} else if (choix == 3) {
				System.out.println("Moyenne: " + calculsRemote.avg());
			} else if (choix == 4) {
				System.out.println("Minimum: " + calculsRemote.min());
			} else if (choix == 5) {
				System.out.println("Maximum: " + calculsRemote.max());
			}
		}
	}
}
