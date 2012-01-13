begin
   delete from client;
   delete from errlog;
   err_pkg.logto (err_pkg.c_screen + err_pkg.c_table + err_pkg.c_file, 'LUDO');

   client_pkg.ajout_client ('Einstein', 'Albert', '14/03/1879');
   dbms_output.put_line ('Ajout correctement effectué');

   dbms_output.put_line ('---------------------------------------------------------');

   client_pkg.ajout_client ('Turing', 'Alan', '23/06/1912');
   dbms_output.put_line ('Ajout correctement effectué');

   dbms_output.put_line ('---------------------------------------------------------');

   client_pkg.ajout_client ('Turing', 'Alan', '23/06/1912');
   dbms_output.put_line ('Ajout correctement effectué');

   dbms_output.put_line ('---------------------------------------------------------');

   client_pkg.ajout_client ('Dupont', 'Albert', '20/07/2006');
   dbms_output.put_line ('Ajout correctement effectué');
exception
	when errnums_pkg.client_ajout_ex then
       dbms_output.put_line (':::: erreur lors de l''ajout du client ::::');
       dbms_output.put_line (':::: ' || sqlerrm || ' ::::');
	when others then
       dbms_output.put_line (':::: erreur inconnue. SQLCODE: ' || SQLCODE || ' , SQLERRM: ' || SQLERRM);
end;
/