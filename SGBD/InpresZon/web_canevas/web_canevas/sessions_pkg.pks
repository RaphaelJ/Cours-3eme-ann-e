create or replace package sessions_pkg as
	type vc_arr is table of varchar2 (4000) index by binary_integer;
	
	session_duration constant number := 120; -- minutes
	
	function is_logged return boolean;
	function check_login (p_lastname in client.nom%type, p_firstname in client.prenom%type, p_password in client.mdp%type) return boolean;
	procedure get_user(p_lastname out client.nom%type, p_firstname out client.prenom%type);
	procedure logout;
end sessions_pkg;
/
