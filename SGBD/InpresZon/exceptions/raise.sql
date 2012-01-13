declare
   too_young exception;
   age integer := 16;
begin
   if age < 18 then
      raise too_young;
   end if;
/* ... */
exception
   when too_young then
      dbms_output.put_line ('Vous êtes trop jeune !');
      dbms_output.put_line ('SQLCODE: ' || SQLCODE || ', SQLERRM: ' || SQLERRM);
end;
