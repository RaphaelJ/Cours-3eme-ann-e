CREATE OR REPLACE PROCEDURE PROCESS_DOWNLOAD is
v_res         owa_text.vc_arr;
v_extension   varchar2 (4);
v_lob_type    varchar2 (10);
v_path        varchar2 (255);

bfile_ex exception;
bfile_const constant number := -20001;
pragma exception_init (bfile_ex, -20001);

blob_ex exception;
blob_const constant number := -20002;
pragma exception_init (blob_ex, -20002);

procedure process_bfile (p_path varchar2) is
	type extensions_type is table of varchar2 (30) index by varchar2 (10);

	v_myfile       bfile;
	v_res          owa_text.vc_arr;
	v_extensions   extensions_type;
begin
	v_extensions ('css')     := 'text/css';
	v_extensions ('png')     := 'image/png';
	v_extensions ('jpg')     := 'image/jpeg';
	v_extensions ('html')    := 'text/html';
	v_extensions ('ttf')    := 'application/x-font-ttf';

	if not (owa_pattern.match (p_path, '\.(\w+)$', v_res)) then
		raise_application_error(bfile_const, 'Pattern matching to extract extension failed');
	end if;

	v_extension := v_res (1);
	v_myfile := bfilename ('KUTY', p_path);
	if (dbms_lob.fileexists(v_myfile) = 0) then
		raise_application_error(bfile_const, 'File does not exist: ' || p_path);
	end if;
	owa_util.mime_header (v_extensions (v_extension));
	wpg_docload.download_file (v_myfile);
end process_bfile;

procedure process_blob (p_path varchar2) is
	v_mime_type images.mime_type%type;
	v_image images.image%type;
	v_id number;
	numeric_or_value_error_ex exception;
	pragma exception_init (numeric_or_value_error_ex, -6502);
begin
	begin
		v_id := to_number(p_path);
	exception
		when numeric_or_value_error_ex then
			v_id := null;
	end;
	if v_id is null then
		select image, mime_type into v_image, v_mime_type from images where name like p_path;
	else
		select image, mime_type into v_image, v_mime_type from images where id = v_id;
	end if;
	owa_util.mime_header (v_mime_type);
	wpg_docload.download_file (v_image);
exception
	when no_data_found then
		raise_application_error(blob_const, 'process_blob: no BLOB in table identified by ' || 
		case 
			when v_id is null then 'NAME: ' || p_path
			else 'PK: ' || p_path
		end
		); 
end process_blob;

begin

	if not (owa_pattern.match (owa_util.get_cgi_env ('PATH_INFO'), '^/(\w+)/(.*)$', v_res)) then
		ipz.report_error('process_download', message => 'Pattern matching to dispatch failed', append_sqlerrm => false);
		return;
	end if;
	
	v_lob_type    := v_res (1);
	v_path        := v_res (2);

	if v_lob_type = 'bfile' then
		process_bfile (v_path);
	elsif v_lob_type = 'blob' then
		process_blob (v_path);
	else
		ipz.report_error('process_download', message => 'No matching dispatching clause: ' || v_lob_type, append_sqlerrm => false);
		return;
	end if;
exception when others then
	ipz.report_error('process_download', append_sqlerrm => true);
	return;
end process_download;
/
 