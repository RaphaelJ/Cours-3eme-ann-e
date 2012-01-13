declare
   i   integer;
begin
   i := 10;
   begin
      i := 1 / 0;
      i := 20;
   exception
      when zero_divide then
         dbms_output.put_line ('Division par zéro !');
   end;
   dbms_output.put_line ('Valeur de i: ' || i);
end;
