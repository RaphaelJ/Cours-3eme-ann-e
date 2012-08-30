import java.sql.{Connection, DriverManager, ResultSet}

object Forage {
    val ORACLE_HOST = "127.0.0.1"
    val ORACLE_PORT = 1521
    val ORACLE_SID = "orcl"
    val ORACLE_USA_USER = "usa"
    val ORACLE_USA_PASS = "pass"
    val ORACLE_MARKETING_USER = "marketing"
    val ORACLE_MARKETING_PASS = "pass"
    
    val connection_usa = init_connexion(ORACLE_USA_USER, ORACLE_USA_PASS)
    val connection_marketing = init_connexion(
        ORACLE_MARKETING_USER, ORACLE_MARKETING_PASS
    )
    
    def init_connexion(user : String, pass : String) : Connection = {
        Class.forName("oracle.jdbc.driver.OracleDriver").newInstance
        DriverManager.getConnection(
            "jdbc:oracle:thin:@"+ORACLE_HOST+":"+ORACLE_PORT+":"+ORACLE_SID,
            user, pass
        )
    }

    def import_
    
    def main(args: Array[String]) {
        if (args.length != 1)
            println("Usage: chargement_usa <output_dir>")
        else {
            
            println(args(0))
        }
    }
}