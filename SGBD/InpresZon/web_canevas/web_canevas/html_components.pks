create or replace package html_components as
	procedure debut(p_title in varchar2);
	procedure banniere(p_info  in varchar2 );
	procedure partieglobale;
	procedure menugaucheconnexion;
	procedure menugauchelienconnexion;
	procedure menugaucheClient;
	procedure menugaucheliendeconnexion;
	procedure menugauchelienaccueil;
	procedure menugaucheconsultation;
	procedure contenuHomePage;
	procedure contenuAccueil;
	procedure contenuConnexion;
	procedure fin;
end html_components;
/
