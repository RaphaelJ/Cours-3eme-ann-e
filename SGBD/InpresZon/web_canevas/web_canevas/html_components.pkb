create or replace package body html_components as

	/* place les balises de debut et construit le header de la page html */
	procedure debut(p_title in varchar2) as
	begin
		htb.reset;
		htb.append_nl('<?xml version="1.0" encoding="iso-8859-1"?>');
		htb.append_nl('<!doctype html public "-//w3c//dtd xhtml 1.0 strict//en" "http://www.w3.org/tr/xhtml1/dtd/xhtml1-strict.dtd">');
		htb.append_nl('<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="fr-be" lang="fr-be">');

		-- header de la page html

		htb.append_nl('<head>');
		htb.append_nl('<title>'||p_title||'</title>');

		-- feuille de style

		htb.append_nl('<link rel="stylesheet" href="/ipz/static/bfile/screen.css" type="text/css" media="screen"/>');

		-- librairies js éventuelles (par exemple...)

		--htb.append_nl('<script type="text/javascript" src="/ipz/static/bfile/date.js"></script>');

		htb.append_nl('</head>');
		htb.append_nl('<body>');
	end debut;

/* construit la bannière de la page */

	procedure banniere (p_info in varchar2) as
	begin
		htb.append_nl('<div id="entete">');
		htb.append_nl('<img id="leftimg" src="/ipz/static/bfile/books.gif" alt="[image bannière 1]" />');
		htb.append_nl('<h1>Inprezon</h1>');
		htb.append_nl('<img id="rightimg" src="/ipz/static/bfile/movies.gif" alt="[image bannière 2]" />');  
		htb.append_nl('</div>');
	end;

	procedure partieglobale as
	begin
    	htb.append_nl('<div id="global">');
	end partieglobale;

	procedure menugaucheconnexion as
	begin
		htb.append_nl('<div id="login" class="blocmenugauche menu">');
		html_forms.connexion;
		htb.append_nl('</div>');
	end menugaucheconnexion;

	procedure menugauchelienconnexion as
	begin
		htb.append_nl('<div class="blocmenugauche menu">');
		htb.append_nl('<ul>');
		htb.append_nl('<li>');
		htb.append_nl('<a href="/ipz/ipz.connexion">connexion</a>');
		htb.append_nl('</li>');
		htb.append_nl('</ul>');
		htb.append_nl('</div>');
	end menugauchelienconnexion;

	procedure menugaucheliendeconnexion as
	begin
		htb.append_nl('<div class="blocmenugauche menu">');
		htb.append_nl('<ul>');
		htb.append_nl('<li>');
		htb.append_nl('<a href="/ipz/ipz.deconnexion">déconnexion</a>');
		htb.append_nl('</li>');
		htb.append_nl('</ul>');
		htb.append_nl('</div>');
	end menugaucheliendeconnexion;

	procedure menugauchelienaccueil as
	begin
		htb.append_nl('<div class="blocmenugauche menu">');
		htb.append_nl('<ul>');
		htb.append_nl('<li>');
		htb.append_nl('<a href="/ipz/ipz.accueil">accueil</a>');
		htb.append_nl('</li>');
		htb.append_nl('</ul>');
		htb.append_nl('</div>');
	end menugauchelienaccueil;

	procedure menugaucheconsultation as
	begin
		htb.append_nl('<div class="blocmenugauche menu">');
		htb.append_nl('<ul>');
		htb.append_nl('<li>');
		htb.append_nl('<span>consultations</span>');
		htb.append_nl('<ul>');
		htb.append_nl('<li>');
		htb.append_nl('<a href="#">visualiser le catalogue</a>');
		htb.append_nl('</li>');
		htb.append_nl('<li>');
		htb.append_nl('<a href="#">rechercher dans le catalogue</a>');
		htb.append_nl('</li>');              
		htb.append_nl('</ul>');
		htb.append_nl('</li>');
		htb.append_nl('</ul>');
		htb.append_nl('</div>');
	end menugaucheconsultation;

	procedure menugaucheclient as
	begin
		htb.append_nl('<div class="blocmenugauche menu">');
		htb.append_nl('<ul>');
		htb.append_nl('<li>');
		htb.append_nl('<span>menu clients</span>');
		htb.append_nl('<ul>');
		htb.append_nl('<li>');
		htb.append_nl('<a href="#">visualiser mes projets</a>');
		htb.append_nl('</li>');
		htb.append_nl('</ul>');
		htb.append_nl('</li>');
		htb.append_nl('</ul>');
		htb.append_nl('</div>');
	end menugaucheclient;

	procedure contenuhomepage as
	begin
		htb.append_nl('<div id="contenu">');
		htb.append_nl('<p>Bienvenue sur le site de FAP</p>');
		htb.append_nl('</div>');
	end contenuhomepage;

	procedure contenuaccueil as
	begin
		htb.append_nl('<div id="contenu">');
		htb.append_nl('<p>Bonjour, choisissez dans le menu ce que vous voulez faire</p>');
		htb.append_nl('</div>');
	end contenuaccueil;

	procedure contenuconnexion as
	begin
		htb.append_nl('<div id="contenu">');
      	htb.append_nl('<p>Merci de bien vouloir vous connecter pour aller plus loin</p>');
		htb.append_nl('</div>');
	end contenuconnexion;

	procedure fin as
	begin
		htb.append_nl('</div>');
		htb.append_nl('<div id="pied">SGBD 2010/2011. Herbiet, Kuty.</div>');
		htb.append_nl('</body>');
		htb.append_nl('</html>');
	end fin;
end;
/
