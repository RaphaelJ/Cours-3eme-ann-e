create or replace package ipz as
	procedure welcome;
	procedure accueil;
	procedure connexion;
	procedure connectUser(p_lastname in client.nom%type, p_firstname in client.prenom%type, p_password in client.mdp%type);
	procedure deconnexion;
	procedure report_error(subprog in varchar2, message in varchar2 := null, append_sqlerrm in boolean := false);
end ipz;
/
