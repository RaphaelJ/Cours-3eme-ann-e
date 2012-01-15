import java.sql.{Connection, DriverManager, ResultSet}

object Forage {
    val ORACLE_HOST = "127.0.0.1"
    val ORACLE_PORT = 1523
    val ORACLE_SID = "oracle"
    val ORACLE_USER = "marketing"
    val ORACLE_PASS = "pass"

    // Un fait représente une source d'informations. Chaque fait peut être
    // accédé via différentes vues.
    class Fait(
        val nom: String, val table: String, val champ: String,
        val vues: Array[Vue]
    )
    // Chaque vue permet de spécifier un groupement (avec, éventuellement, 
    // plusieurs niveaux de détail).
    class Vue(val nom: String, val details: Array[Detail])
    // Permet de spécifier le détail d'une vue
    class Detail(val nom: String, val champ: String)
    
    val connection = init_connexion()
    
    def init_connexion(): Connection = {
        Class.forName("oracle.jdbc.driver.OracleDriver").newInstance
        DriverManager.getConnection(
            "jdbc:oracle:thin:@"+ORACLE_HOST+":"+ORACLE_PORT+":"+ORACLE_SID,
            ORACLE_USER, ORACLE_PASS
        )
    }

    def menu[T](titre: String, fcts: Array[(String, () => T)]) = {
        def go(): List[T] = {
            println(titre+" :")
            for (((msg, f), i) <- fcts zipWithIndex)
                println((i+1) + ". " + msg)
            println("0. Retour")
            
            print("Votre choix: ")
            val choix = Console.readInt() - 1
            
            if (choix >= 0 && choix < fcts.length)
                fcts(choix)._2() :: go()
            else
                Nil
        }
        go.reverse
    }
    
    def joindre(chaines: Iterable[String], glue: String) = 
        chaines.reduceLeft(_ + glue + _)

    // Permet d'effectuer un forage sur un fait en questionnant l'utilisateur 
    // sur les vues à appliquer.
    def forer(fait: Fait) {
        // Demande les vues à activer
        val vues = fait.vues.foldLeft(Nil: List[(Vue, Detail)])((acc, vue) => {
            val config_vue = configurer_vue(vue)
            if (config_vue != null)
                (vue, config_vue) :: acc
            else // Vue désactivée
                acc
        })
        
        // Affiche les résultats
        executer_sql(construire_sql(fait, vues))
    }

    // Demande à l'utilisateur la configuration éventuelle d'une vue et retourne
    // le détail choisi.
    def configurer_vue(vue: Vue) = {
        println("Souhaitez-vous activer la vue « "+vue.nom+" » ? (O/N)")
        
        val choix = Console.readChar()
        if (choix == 'O' || choix == 'o') 
            configurer_details(vue.details)
        else
            null 
    }

    // Demande à l'utilisateur ne niveau de détail d'une vue
    def configurer_details(details: Array[Detail]) = {
        if (details.length > 1) {
            println("Niveau de détail: ")
            for ((detail, i) <- details zipWithIndex)
                println((i+1) + ". " + detail.nom)

            val choix = Console.readInt() - 1
            details(choix)
        } else // Un seul détail: pas de choix
            details(0)
    }

    // Retourne la requête SQL avec les titres des champs retournés depuis la
    // configuration du fait et de ses vues.
    def construire_sql(fait: Fait, vues: List[(Vue, Detail)]) = {
        val joindre_champs = joindre(_: Iterable[String], ", ")
        
        // Retourne le titre et te champ de la vue
        def champs_vue(vue: (Vue, Detail)) = {
            if (vue._2.nom == null)
                (vue._1.nom, vue._2.champ)
            else
                (vue._1.nom+" ("+vue._2.nom+")", vue._2.champ)
        }

        // Retourne les noms des champs dans la table
        def champs(colonnes: Iterable[(String, String)]) = 
            colonnes.map(i => i._2)
        
        // (titre de la colonne, nom dans la table)
        val colonnes_vues = vues map champs_vue
        val colonnes = (fait.nom, fait.champ) :: colonnes_vues.toList
        val group_by =
            if (colonnes_vues.length > 0) 
                "GROUP BY " + joindre_champs(champs(colonnes_vues))
            else 
                " "
        
        ("SELECT " + joindre_champs(champs(colonnes)) + " " +
         "FROM "+ fait.table + " " +
         group_by, colonnes)
    }

    // Exécute la requête SQL et affiche un tableau avec les titres et les noms
    // des colonnes fournis en argument.
    def executer_sql(requete: (String, Iterable[(String, String)])) = {
        val en_tete = joindre(requete._2.map(i => i._1), " | ")
        
        // Affiche l'en-tête du tableau
        println(en_tete)
        println("-" * en_tete.length)

        // Affiche les résultats
        val resultSet = connection.prepareStatement(requete._1).executeQuery()
        while (resultSet.next) {
            // Affiche toutes les colonnes
            for ((col, i) <- requete._2 zipWithIndex) {
                val res = resultSet.getString(i+1)
                val padding = col._1.length - res.length + 3
                print(res + " " * padding)
            }
            println()
        }

        println("-" * en_tete.length)
    }
    
    def main(args: Array[String]) {
        // Vues utilisées
        val vue_type_media = new Vue(
            "Type de média", Array(new Detail(null, "type"))
        )
        val vue_origine = new Vue(
            "Origine", Array(new Detail(null, "origine"))
        )
        val vue_periode_trimestre_annee = new Vue("Période", Array(
            new Detail(
                "Trimestre", 
                "floor(EXTRACT(MONTH FROM date_commande) / 3) + 1"
            ),
            new Detail("Année", "EXTRACT(YEAR FROM date_commande)")
        ))
        val vue_periode_mois_annee = new Vue("Période", Array(
            new Detail("Mois", "EXTRACT(MONTH FROM date_commande)"),
            new Detail("Année", "EXTRACT(YEAR FROM date_commande)")
        ))
        val vue_langue = new Vue("Langue", Array(new Detail(null, "langue")))
        val vue_article = new Vue("Article", Array(new Detail(null, "ean")))
        val vue_utilisateur = new Vue(
            "Utilisateur", Array(new Detail(null, "login"))
        ) 
        
        // Faits disponibles
        val faits = Array(
            new Fait(
                "Quantité de vente des médias", "vue_ventes", "SUM(quantite)",
                Array(vue_type_media, vue_origine, vue_periode_trimestre_annee)
            ),
            new Fait(
                "Pourcentage des ventes des livres", "vue_ventes_livres",
                "SUM(quantite_livres) / SUM(quantite) * 100",
                Array(vue_langue, vue_origine, vue_periode_mois_annee)
            ),
            new Fait(
                "Fréquence des nouveaux stocks", "vue_frequence_stocks",
                "COUNT(*)", Array(
                    vue_type_media, vue_article, vue_periode_trimestre_annee
                )
            ),
            new Fait(
                "Commentaires apportés", "vue_commentaires_apportes",
                "COUNT(*)", Array(
                    vue_utilisateur, vue_type_media/*, vue_origine*/,
                    vue_periode_mois_annee
                )
            ),
            new Fait(
                "Quantité moyene d'article commandés par commande",
                "vue_ventes", "AVG(quantite)", Array(
                    vue_utilisateur, vue_type_media, vue_origine
                )
            ),
            new Fait(
                "Proportion des ventes depuis les listes d'envies",
                "vue_ventes", "SUM(dans_liste_envie) / COUNT(*)",
                Array(
                    vue_utilisateur, vue_type_media, vue_origine,
                    vue_periode_mois_annee
                )
            )
        )
        
        // Crée un élément du menu pour un fait
        def fait_menu(fait: Fait) =
            (fait.nom, () => forer(fait))
        
        menu("Faits", faits map fait_menu)
    }
}