INSERT INTO site_produit VALUES (3, 'Imagine', 'Imagine', 'EN', 12.12, 'EUR', 1);
INSERT INTO site_livre VALUES (3, "aaaaaaaaaaaa", 3, 'collage', 120, '2004-12-12', 1);

SELECT * FROM site_livre AS l INNER JOIN site_produit AS p ON l.produit_ptr_id = p.id;

SELECT * FROM site_livre;
SELECT * FROM site_produit;
