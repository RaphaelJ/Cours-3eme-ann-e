create or replace package body html_forms as
	procedure connexion as
	begin
		htb.append_nl(
			'<form id="FormuConnexion" action="/ipz/ipz.connectUser" method="post">'
		);
		htb.append_nl('<ul>');
		htb.append_nl('<li>');
		htb.append_nl('<label for="lastname">Last name:</label><br/>');
		htb.append_nl('<input type="text" id="lastname" name="p_lastname" />');
		htb.append_nl('</li>');
		htb.append_nl('<li>');
		htb.append_nl('<li>');
		htb.append_nl('<label for="firstname">First name:</label><br/>');
		htb.append_nl('<input type="text" id="firstname" name="p_firstname" />');
		htb.append_nl('</li>');
		htb.append_nl('<li>');
		htb.append_nl('<label for="password">Password:</label><br/>');
		htb.append_nl('<input type="password" id="password" name="p_password" />');
		htb.append_nl('</li>');
		htb.append_nl('<li>');
		htb.append_nl('<input type="submit" value="Se connecter"/>');
		htb.append_nl('</li>');
		htb.append_nl('</ul>');
		htb.append_nl('</form>');
	end connexion;
end html_forms;
/
