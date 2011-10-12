create or replace package body sessions_pkg as
/**************************************************************
 * GET_SESSION_ID (PRIVATE)
 * Get value of cookie sessionid if a valid session exists.
 * Else returns NULL.
 * Update the last access time of this session.
 **************************************************************/
	function get_session_id return varchar2 is
		session_cookie   owa_cookie.cookie;
		session_id       varchar2 (32);
		last             date;
	begin
		session_cookie := owa_cookie.get ('sessionid');
		
		if session_cookie.num_vals = 0 then
		   return null;
		end if;
		
		session_id := session_cookie.vals (session_cookie.vals.first);
		
		begin
		   select sessions.last
		     into last
		     from sessions
		    where sessions.id = session_id;
		exception
		   when no_data_found then
		      owa_cookie.remove ('sessionid', session_id);
		      return null;
		end;
		
		if sysdate > last + sessions_pkg.session_duration / 1440 then
			owa_cookie.remove ('session_id', session_id);
			delete from sessions where id = session_id;
			commit;
 			return null;
		end if;
		
		update sessions
		   set last = sysdate
		 where id = session_id;
		
		return session_id;
	end get_session_id;
	
	procedure logout is
		v_session_id varchar2 (32);
	begin
		v_session_id := get_session_id;
		if v_session_id is null then
			return;
		end if;
		owa_cookie.remove ('session_id', v_session_id);
		delete from sessions where id = v_session_id;
		commit;
	end logout;

	function check_login (p_lastname in client.nom%type, p_firstname in client.prenom%type, p_password in client.mdp%type) return boolean is
		v_session_id      varchar2 (32);
		v_client_id       client.id%type;
	begin
		v_session_id := get_session_id;
		if v_session_id is not null then
			return false;
		end if;
		
		begin
			select id into v_client_id
			from client
			where nom like p_lastname and prenom like p_firstname and mdp like p_password;
		exception when no_data_found then
			return false;
		end;
		
		select sys_guid () into v_session_id from dual;
			
		owa_cookie.send ('sessionid', v_session_id, sysdate + 30 / 1440);
			
		insert into sessions values (v_session_id, v_client_id, sysdate);
		commit;
		return true;
	end check_login;
	
	function is_logged return boolean is
		v_session_cookie owa_cookie.cookie;
		v_session_id     varchar2 (32);
	begin
		v_session_id := get_session_id;
		
		if v_session_id is not null then
			return true;
		end if;
		return false;
	end is_logged;
  
	procedure get_user(p_lastname out client.nom%type, p_firstname out client.prenom%type) is
		v_session_id     varchar2 (32);
	begin
		v_session_id := get_session_id;
		
		if v_session_id is not null then
			select nom, prenom into p_lastname, p_firstname from client, sessions where sessions.client = client.id and sessions.id = v_session_id;
		end if;
	end get_user;
end sessions_pkg;
/
