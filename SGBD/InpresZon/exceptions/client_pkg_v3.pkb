create or replace package body client_pkg is
   procedure ajout_client (nom varchar2, prenom varchar2, naissance varchar2) is
      naissance_date   date;
	
	  validation_ex exception;
	  validation_const constant number := -20001;
	  pragma exception_init (validation_ex, -20001);
	
	  insertion_ex exception;
	  insertion_const constant number      := -20002;
	  pragma exception_init (insertion_ex, -20002);

/**************************************************************
 * VALIDATION
 **************************************************************/
      procedure validation is
      begin
         begin
            naissance_date    := to_date (naissance, 'DD/MM/YYYY');
         exception
            when others then
			   raise_application_error(-20001, 'Date de naissance incorrecte: ' || naissance || '.');
         end;

         if (sysdate - naissance_date < 12 * 365) then
            raise_application_error(-20001, 'Personne trop jeune càd < 12 ans.');
         end if;
      end;

/**************************************************************
 * INSERTION
 **************************************************************/
      procedure insertion is
      begin
         insert into client
              values (nom, prenom, naissance_date);
         commit;
      exception
         when others then
            rollback;
            raise_application_error(-20002, err_pkg.get_error_message(default_message => 'échec lors de l''insertion'));
      end;
/**************************************************************
 * MAIN CODE
 **************************************************************/
   begin
      validation;
      insertion;
   exception
      when validation_ex then
         err_pkg.report_and_go('client_pkg.ajout_client', errnums_pkg.client_ajout_const, strip_code => true);
      when insertion_ex then
         err_pkg.report_and_go('client_pkg.ajout_client', errnums_pkg.client_ajout_const, strip_code => true);
      when others then
         err_pkg.report_and_go('client_pkg.ajout_client', errnums_pkg.client_ajout_const, 'Problème inconnu', true);
   end;
end client_pkg;
/
