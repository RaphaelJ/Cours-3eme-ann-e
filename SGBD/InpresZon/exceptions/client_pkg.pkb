create or replace package body client_pkg is
   procedure ajout_client (nom varchar2, prenom varchar2, naissance varchar2) is
      naissance_date   date;

/**************************************************************
 * VALIDATION
 **************************************************************/
      procedure validation is
      begin
         begin
            naissance_date    := to_date (naissance, 'DD/MM/YYYY');
         exception
            when others then
               err_pkg.report_and_stop ('add_new_client::validation',
                                        errnums_pkg.validation_const,
                                        'date de naissance incorrecte: ' || naissance,
                                        true
                                       );
         end;

         if (sysdate - naissance_date < 12 * 365) then
            err_pkg.report_and_stop ('add_new_client::validation',
                                     errnums_pkg.validation_const,
                                     'personne trop jeune càd < 12 ans'
                                    );
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
            err_pkg.report_and_stop ('add_new_client::insertion',
                                     errnums_pkg.insertion_const,
                                     'échec lors de l''insertion',
                                     true
                                    );
      end;
/**************************************************************
 * MAIN CODE
 **************************************************************/
   begin
      validation;
      insertion;
   end;
end client_pkg;
/
