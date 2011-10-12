create or replace package body htb as
	buffer clob;
	
	procedure append(line varchar2) is
	begin
		dbms_lob.writeappend(buffer, length(line), line);
	end append;
	
	procedure append_nl(line varchar2) is
	begin
		dbms_lob.writeappend(buffer, length(line), line);
		dbms_lob.writeappend(buffer, 1, chr(10));
	end append_nl;
		
	procedure reset is
		--amount integer; --dbms_lob.lobmaxsize;
	begin
		--amount := dbms_lob.getlength(buffer);
		--dbms_lob.erase(buffer, amount);
		dbms_lob.trim(buffer, 0);
	end reset;

	procedure send(content_type varchar2 default 'text/html', status_code integer default 200) is
		v_text varchar2(80);
		len pls_integer;
		pos pls_integer := 1;
	begin
		owa_util.status_line(status_code, bclose_header => false);
		owa_util.mime_header(content_type, bclose_header => false);
		owa_util.http_header_close;
		
		len := dbms_lob.getlength (buffer);
		while pos <= len loop
			v_text := dbms_lob.substr(buffer, 80, pos);
			htp.prn(v_text);
			pos := pos + 80;
		end loop;
		reset;
	end send;
begin
	 dbms_lob.createtemporary (buffer, cache => true, dur => dbms_lob.session);
end htb;
/