execute CHARGEMENTDONNEES.CHARGEMENTBE();
execute CHARGEMENTDONNEES.CHARGEMENTUSA();
execute CHARGEMENTDONNEES.CHARGEMENTUK();
execute DBMS_SNAPSHOT.REFRESH('vue_ventes');
execute DBMS_SNAPSHOT.REFRESH('vue_ventes_livres');
execute DBMS_SNAPSHOT.REFRESH('vue_commentaires_apportes');
execute DBMS_SNAPSHOT.REFRESH('vue_frequence_stocks');
commit;