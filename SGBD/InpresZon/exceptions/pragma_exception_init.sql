declare
   msg varchar2 (20);
   string_too_small exception;
   pragma exception_init (string_too_small, -6502);
begin
   msg := 'Une chaîne de taille supérieure à 20';
exception
   when string_too_small then
      dbms_output.put_line ('Chaîne de caractères de taille trop petite !');
      rollback;
end;
