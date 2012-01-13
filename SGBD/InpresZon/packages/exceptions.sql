create or replace
package err_pkg
is
        c_table constant pls_integer := 1; -- Default
        c_file constant pls_integer := 2;
        c_screen constant pls_integer := 4;

        procedure report_and_stop (
           subprog in varchar2,
           errcode in integer := sqlcode,
           errmsg  in varchar2 := null,
           append_sqlerrm in boolean := false,
           strip_code in boolean := false
        );

        procedure report_and_go (
           subprog in varchar2,
           errcode in integer := sqlcode,
           errmsg  in varchar2 := null,
           append_sqlerrm in boolean := false,
           strip_code in boolean := false
        );

        procedure logto (
           target in pls_integer,
           dir    in varchar2 := null,
           file   in varchar2 := null
        );

        procedure assert (
           condition       in boolean,
           message         in varchar2,
           raise_exception in boolean := true,
           exception_name  in varchar2 := 'VALUE_ERROR'
        );
        
        --function get_error_message (default_message in my_constraints.message%type)
        --sreturn my_constraints.message%TYPE;
end err_pkg;

create or replace
package body err_pkg is
   g_target pls_integer     := c_table;
   g_file   varchar2 (2000) := 'errors.log';
   g_dir    varchar2 (2000) := null;

   /**************************************************************
    * PRIVATE PROCEDURES AND FUNCTIONS
    **************************************************************/
        procedure log (errcode in pls_integer := null, errmsg in varchar2 := null);

        procedure raise (errcode in pls_integer := null, errmsg in varchar2 := null);

        procedure handle (
        errcode in pls_integer := null,
      errmsg  in varchar2 := null,
      logerr  in boolean := true,
      reraise in boolean := false
   ) is
   begin
      if logerr then
         log (errcode, errmsg);
      end if;

      if reraise then
         err_pkg.raise (errcode, errmsg);
      end if;
   end;

        function format_message (
                subprog in varchar2,
                errcode in integer,
                errmsg  in varchar2,
                append_sqlerrm in boolean,
                strip_code in boolean
        )
        return varchar2 is
                message   varchar2 (1000);
                sql_error_message varchar2(512) := sqlerrm;
                errmsg_cleaned varchar2(1000) := errmsg;
                append_sqlerrm_cleaned boolean := append_sqlerrm;
        begin
                -- clean parameters
                if strip_code then
                        owa_pattern.change(sql_error_message, '^ORA-\d+: ', '');
                end if;
                
                if errmsg is null then
                        errmsg_cleaned := sql_error_message;
                        append_sqlerrm_cleaned := false;
                end if;
                
                message := 'SUBPROG: ' || subprog || '. '
                        || 'CODE: ' || errcode || '. '
                                || 'MSG: ' || errmsg_cleaned;

      if append_sqlerrm_cleaned then
         message := message || '. SYSMSG: ' || sql_error_message;
      end if;

      return message;
   end;

   procedure raise (errcode in pls_integer := null, errmsg in varchar2 := null) is
      l_errcode pls_integer     := nvl (errcode, sqlcode);
      l_errmsg  varchar2 (1000) := nvl (errmsg, sqlerrm);
   begin
      if l_errcode between -20999 and -20000 then
         raise_application_error (l_errcode, l_errmsg);
      /* Use positive error numbers -- lots to choose from! */
      elsif l_errcode > 0 and l_errcode not in (1, 100) then
         raise_application_error (-20000, l_errcode || '-' || l_errmsg);
      /* Can't EXCEPTION_INIT -1403 */
      elsif l_errcode in (100, -1403) then
         raise no_data_found;
      /* Re-raise any other exception. */
      elsif l_errcode != 0 then
         execute immediate    'DECLARE myexc EXCEPTION; '
                           || '   PRAGMA EXCEPTION_INIT (myexc, '
                           || to_char (l_errcode)
                           || ');'
                           || 'BEGIN  RAISE myexc; END;';
      end if;
   end;

   procedure log (errcode in pls_integer := null, errmsg in varchar2 := null) is
       /*
       The AUTONOMOUS_TRANSACTION pragma changes the way a subprogram works
      within a transaction. A subprogram marked with this pragma can do SQL
      operations and commit or roll back those operations, without committing
      or rolling back the data in the main transaction.
       */
      pragma autonomous_transaction;
      l_sqlcode pls_integer     := nvl (errcode, sqlcode);
      l_sqlerrm varchar2 (1000) := nvl (errmsg, sqlerrm);
   begin
      if bitand (g_target, c_table) = c_table then
         insert into errlog
                     (errcode, errmsg, created_on, created_by
                     )
              values (l_sqlcode, l_sqlerrm, sysdate, user
                     );
         commit;
      end if;

      if bitand (g_target, c_file) = c_file then
         declare
            fid   utl_file.file_type;
         begin
            fid    := utl_file.fopen (g_dir, g_file, 'A');
            utl_file.put_line (fid,
                                  'Error log by '
                               || user
                               || ' at  '
                               || to_char (sysdate, 'dd/mm/yyyy HH:MI:SS')
                              );
            utl_file.put_line (fid, nvl (errmsg, sqlerrm));
            utl_file.new_line (fid);
            utl_file.fclose (fid);
         exception
            when others then
               utl_file.fclose (fid);
         end;
      end if;

      if bitand (g_target, c_screen) = c_screen then
         dbms_output.put_line (   'Error log by '
                               || user
                               || ' at  '
                               || to_char (sysdate, 'dd/mm/yyyy HH:MI:SS')
                              );
         dbms_output.put_line (nvl (errmsg, sqlerrm));
         dbms_output.new_line;
      end if;
   exception
      when others then
         dbms_output.put_line ('err_pkg::log : INTERNAL ERROR ! ' || SQLERRM);
   end;

        /*function find_constraint_name return my_constraints.name%TYPE
        as
                res     owa_text.vc_arr;
                found   boolean;
        begin
                -- not null. check constraint with code -1400. special case.
                if sqlcode = -1400 then
                        -- FORMAT: ("SCHEMA"."TABLE"."COLUMN")
                        found := owa_pattern.match (sqlerrm, '\("(.*)"\."(.*)"\."(.*)"\)', res);
                        if found then
                                return res (2) || '_' || res (3) || '_nn';
                        end if;
                elsif sqlcode <> 0 then
                        -- FORMAT: (SCHEMA.CONSTRAINT)
                        found := owa_pattern.match (sqlerrm, '\(\w+\.(.*)\)', res);
                        if found then
                                return res (1);
                        end if;
                end if;
                return null;
        end find_constraint_name;

        function get_constraint_message (constraint_name in my_constraints.name%TYPE)
        return my_constraints.message%TYPE
        as
                message my_constraints.message%TYPE;
        begin
                select mc.message into message from my_constraints mc
                where mc.name = lower(constraint_name);
                return message;
        exception when no_data_found then
                return null;
        end get_constraint_message;*/
        
   /**************************************************************
    * PUBLIC PROCEDURES AND FUNCTIONS
    **************************************************************/
   procedure report_and_stop (
      subprog in varchar2,
      errcode in integer := sqlcode,
      errmsg  in varchar2 := null,
      append_sqlerrm in boolean := false,
          strip_code in boolean := false
   ) is
        begin
      handle (errcode,
              format_message (subprog, errcode, errmsg, append_sqlerrm, strip_code),
              true,
              true
             );
   end report_and_stop;

   procedure report_and_go (
      subprog in varchar2,
      errcode in integer := sqlcode,
      errmsg  in varchar2 := null,
      append_sqlerrm in boolean := false,
      strip_code in boolean := false
   ) is
   begin
      handle (errcode,
              format_message (subprog, errcode, errmsg, append_sqlerrm, strip_code),
              true,
              false
             );
   end report_and_go;

   procedure logto (
      target   in   pls_integer,
      dir      in   varchar2 := null,
      file     in   varchar2 := null
   ) is
   begin
      g_target    := target;
      g_file      := nvl(file, g_file);
      g_dir       := dir;
   end;

   procedure assert (
      condition       in boolean,
      message         in varchar2,
      raise_exception in boolean := true,
      exception_name  in varchar2 := 'VALUE_ERROR'
   ) is
   begin
      if not condition or condition is null then
         dbms_output.put_line ('Assertion Failure!');
         dbms_output.put_line (message);
                 --log (errcode, 'Assertion failure: ' || errmsg);

         if raise_exception then
            execute immediate 'BEGIN RAISE ' || exception_name || '; END;';
         end if;
      end if;
   end assert;
        
        --function get_error_message (default_message in my_constraints.message%type)
        --return my_constraints.message%TYPE
        --as
        --      message my_constraints.message%TYPE;
        --begin
        --      message := err_pkg.get_constraint_message(err_pkg.find_constraint_name);
        --      message := nvl(message, default_message);
        --      return message;
        --end get_error_message;
end;

CREATE OR REPLACE package errnums_pkg
is
    fournisseur_not_found_ex exception;
    fournisseur_const number := := -20002;
    pragma exception_init (fournisseur_not_found_ex, -20002);
end errnums_pkg;