create or replace package body ipz as
	/* Construit la page de bienvenue et fait appel aux composants d'une page HTML */
	procedure welcome as
	begin
		html_components.debut('FAP: welcome');
		html_components.banniere('Non connecté');
		html_components.partieglobale;
		html_components.menugauchelienconnexion;
		html_components.menugaucheconsultation;
		html_components.contenuHomePage;
		html_components.fin;
		htb.send;
	exception when others then
		report_error(subprog => 'ipz.welcome');
	end;

	/* Construit la page de connexion appelée par SIGN IN ou par le lien de connexion de la page welcome */
	procedure connexion  as
	begin
		if sessions_pkg.is_logged then
			owa_util.redirect_url('/ipz/ipz.accueil');
			return;
		end if;
		
		html_components.debut('FAP: connexion');
		html_components.banniere('Non connecté');
		html_components.partieglobale;
		html_components.menugaucheconnexion;
		html_components.menugaucheconsultation;       
		html_components.contenuconnexion;
		html_components.fin;
		htb.send;
	exception when others then
		report_error(subprog => 'ipz.connexion');
	end;

	procedure connectUser(p_lastname in client.nom%type, p_firstname in client.prenom%type, p_password in client.mdp%type) as
	begin
		if sessions_pkg.is_logged then
			owa_util.redirect_url('/ipz/ipz.accueil');
			return;
		end if;
		
		if not sessions_pkg.check_login(p_lastname, p_firstname, p_password) then
			report_error(subprog => 'ipz.connectUser', message => 'Utilisateur inconnu ou mot de passe incorrect. Veuillez recommencer');
			return;
		end if;

		owa_util.redirect_url('/ipz/ipz.accueil');
	exception when others then report_error(subprog => 'ipz.connectUser'); 
	end connectUser;
	
/* Déconnecte un utilisateur et reconstruit la page d'accueil */
	procedure deconnexion as
	begin
		sessions_pkg.logout;
		owa_util.redirect_url('/ipz/ipz.welcome');
	exception when others then report_error(subprog => 'ipz.deconnexion'); 
	end deconnexion;

	/* Cette procedure construit la page d'accueil d'un utilisateur déjà connecté */
	procedure accueil as
		v_nomutil client.nom%type;
		v_prenomutil client.prenom%type;
	begin
		if not sessions_pkg.is_logged then
			owa_util.redirect_url('/ipz/ipz.welcome');
			return;
		end if;
		sessions_pkg.get_user(v_nomutil, v_prenomutil);
		html_components.debut('FAP: accueil');
		html_components.banniere('Bienvenue '||v_prenomutil||' '||v_nomutil);
		html_components.partieglobale;
		html_components.menugaucheliendeconnexion;
		html_components.menugauchelienaccueil;
		html_components.menugaucheconsultation;
		html_components.menugaucheClient;
		html_components.contenuAccueil;
		html_components.fin;
		htb.send;
	exception when others then report_error(subprog => 'ipz.accueil');
	end accueil;
	
	procedure report_error(subprog in varchar2, message in varchar2 := null, append_sqlerrm in boolean := false) is
		v_nomutil client.nom%type;
		v_prenomutil client.prenom%type;
		banner varchar2(100) := 'Non connecté';
	begin
			sessions_pkg.get_user(v_nomutil, v_prenomutil);
			html_components.debut('FAP: error');
			if v_nomutil is not null then
				banner := 'Bienvenue '||v_prenomutil||' '||v_nomutil;
			end if;
			html_components.banniere(banner);
			html_components.partieglobale;
			if v_nomutil is not null then
				html_components.menugaucheliendeconnexion;
				html_components.menugauchelienaccueil;
				html_components.menugaucheconsultation;
				html_components.menugaucheClient;
			else
				html_components.menugauchelienconnexion;
				html_components.menugaucheconsultation;
				html_components.contenuHomePage;
			end if;
			htb.append_nl('<div id="contenu">');
			htb.append_nl('<h1 style="color: red; border-bottom: 1px solid;">Une erreur s''est produite !</h1>');
			htb.append_nl('<ul>');
			htb.append_nl('<li>SUBPROG: ' || subprog || '</li>');
			htb.append_nl('<li>MESSAGE: ' || htf.escape_sc(nvl(message, sqlerrm)) || '</li>');
			htb.append_nl('</ul>');
			htb.append_nl('</div>');
			html_components.fin;
			htb.send(status_code => 404);
	end report_error;
end ipz;
/
