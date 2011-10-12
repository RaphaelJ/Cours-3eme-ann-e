create or replace procedure purge_sessions as
begin
	delete from sessions
	where sysdate > last + sessions_pkg.session_duration / 1440;
	commit;
end purge_sessions;
/
