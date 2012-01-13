begin
   raise zero_divide;
exception
   when others then
      dbms_output.put_line ('SQLCODE: ' || sqlcode);
      dbms_output.put_line ('SQLERRM: ' || sqlerrm);
      dbms_output.put_line (dbms_utility.format_error_stack);
end;
