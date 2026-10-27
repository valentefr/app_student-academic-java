-- Seed data for db_students_frv (app-students / com.vflores)
-- Generated test data, idempotent: explicit ids + ON CONFLICT DO NOTHING,
-- identity sequences realigned at the end.
--
-- Run with:
--   psql -h localhost -U postgres -d db_students_frv -f db/seed-data.sql
--
-- Table/column names match the Hibernate `ddl-auto: update` schema.

SET client_encoding = 'UTF8';

BEGIN;

-- 1. Independent entities -----------------------------------------------------

-- Student
INSERT INTO student (id_student, first_name, last_name, dni, birth_date, email) VALUES
    ( 1, 'Valentina', 'Flores',    '30124578', '2003-04-12', 'valentina.flores@frv.utn.edu.ar'),
    ( 2, 'Mateo',     'Gimenez',   '31458790', '2002-11-03', 'mateo.gimenez@frv.utn.edu.ar'),
    ( 3, 'Camila',    'Rodriguez', '32785641', '2004-01-27', 'camila.rodriguez@frv.utn.edu.ar'),
    ( 4, 'Lucas',     'Benitez',   '29874103', '2001-07-19', 'lucas.benitez@frv.utn.edu.ar'),
    ( 5, 'Martina',   'Acosta',    '33012947', '2004-09-05', 'martina.acosta@frv.utn.edu.ar'),
    ( 6, 'Thiago',    'Sosa',      '31697452', '2003-02-14', 'thiago.sosa@frv.utn.edu.ar'),
    ( 7, 'Emilia',    'Quiroga',   '30548216', '2002-06-30', 'emilia.quiroga@frv.utn.edu.ar'),
    ( 8, 'Benjamin',  'Ferreyra',  '32140985', '2003-12-21', 'benjamin.ferreyra@frv.utn.edu.ar'),
    ( 9, 'Isabella',  'Luna',      '33457018', '2005-03-08', 'isabella.luna@frv.utn.edu.ar'),
    (10, 'Joaquin',   'Molina',    '29631478', '2001-10-11', 'joaquin.molina@frv.utn.edu.ar'),
    (11, 'Renata',    'Cabrera',   '31985023', '2003-05-23', 'renata.cabrera@frv.utn.edu.ar'),
    (12, 'Santiago',  'Peralta',   '30267941', '2002-08-17', 'santiago.peralta@frv.utn.edu.ar'),
    (13, 'Delfina',   'Ibarra',    '32598647', '2004-04-18', 'delfina.ibarra@frv.utn.edu.ar'),
    (14, 'Bruno',     'Villalba',  '31074592', '2003-09-29', 'bruno.villalba@frv.utn.edu.ar'),
    (15, 'Catalina',  'Maldonado', '33186470', '2004-12-02', 'catalina.maldonado@frv.utn.edu.ar'),
    (16, 'Tomas',     'Aguirre',   '30793145', '2002-03-26', 'tomas.aguirre@frv.utn.edu.ar'),
    (17, 'Julieta',   'Ramos',     '32045718', '2003-11-13', 'julieta.ramos@frv.utn.edu.ar'),
    (18, 'Ignacio',   'Bustos',    '29458307', '2001-01-09', 'ignacio.bustos@frv.utn.edu.ar'),
    (19, 'Victoria',  'Cordoba',   '33620184', '2005-07-24', 'victoria.cordoba@frv.utn.edu.ar'),
    (20, 'Facundo',   'Herrera',   '31302856', '2003-06-07', 'facundo.herrera@frv.utn.edu.ar')
ON CONFLICT (id_student) DO NOTHING;

-- Course
INSERT INTO course (id_course, name, acronym, enabled) VALUES
    ( 1, 'Analisis Matematico I',             'AM1',  TRUE),
    ( 2, 'Algebra y Geometria Analitica',     'AGA',  TRUE),
    ( 3, 'Fisica I',                          'FIS1', TRUE),
    ( 4, 'Algoritmos y Estructuras de Datos', 'AED',  TRUE),
    ( 5, 'Arquitectura de Computadoras',      'ARQ',  TRUE),
    ( 6, 'Sistemas Operativos',               'SO',   TRUE),
    ( 7, 'Bases de Datos',                    'BD',   TRUE),
    ( 8, 'Ingenieria de Software',            'ISW',  TRUE),
    ( 9, 'Redes de Datos',                    'RED',  FALSE),
    (10, 'Inteligencia Artificial',           'IA',   TRUE)
ON CONFLICT (id_course) DO NOTHING;

-- SchoolTerm (unique on year + semester)
INSERT INTO school_term (id_school_term, year, semester) VALUES
    (1, 2024, 1),
    (2, 2024, 2),
    (3, 2025, 1),
    (4, 2025, 2)
ON CONFLICT (id_school_term) DO NOTHING;

-- 2. Dependent entities ------------------------------------------------------

-- Enroll (depends on Student)
INSERT INTO enroll (id_enrolled, id_student, date, enabled) VALUES
    ( 1,  1, '2024-03-04', TRUE),
    ( 2,  2, '2024-03-04', TRUE),
    ( 3,  3, '2024-03-06', TRUE),
    ( 4,  4, '2024-08-05', TRUE),
    ( 5,  5, '2024-08-05', FALSE),
    ( 6,  6, '2024-08-07', TRUE),
    ( 7,  7, '2025-03-03', TRUE),
    ( 8,  8, '2025-03-03', TRUE),
    ( 9,  9, '2025-03-05', TRUE),
    (10, 10, '2025-03-05', FALSE),
    (11, 11, '2025-08-04', TRUE),
    (12, 12, '2025-08-04', TRUE),
    (13, 13, '2025-08-06', TRUE),
    (14, 14, '2025-08-06', TRUE),
    (15, 15, '2025-08-08', TRUE)
ON CONFLICT (id_enrolled) DO NOTHING;

-- EnrollDetail (composite PK: id_school_term + id_enrolled + id_course)
INSERT INTO enroll_detail (id_school_term, id_enrolled, id_course, class_room) VALUES
    (1,  1,  1, 'Aula 101'),
    (1,  1,  2, 'Aula 102'),
    (1,  1,  4, 'Lab Informatica 1'),
    (1,  2,  1, 'Aula 101'),
    (1,  2,  3, 'Lab Fisica'),
    (1,  3,  2, 'Aula 102'),
    (1,  3,  4, 'Lab Informatica 1'),
    (2,  4,  3, 'Lab Fisica'),
    (2,  4,  5, 'Lab Informatica 2'),
    (2,  5,  4, 'Lab Informatica 1'),
    (2,  5,  6, 'Aula 203'),
    (2,  6,  5, 'Lab Informatica 2'),
    (2,  6,  7, 'Lab Informatica 3'),
    (3,  7,  6, 'Aula 203'),
    (3,  7,  7, 'Lab Informatica 3'),
    (3,  7,  8, 'Aula 204'),
    (3,  8,  7, 'Lab Informatica 3'),
    (3,  8, 10, 'Lab Informatica 4'),
    (3,  9,  8, 'Aula 204'),
    (3,  9,  1, 'Aula 101'),
    (3, 10,  2, 'Aula 102'),
    (3, 10,  4, 'Lab Informatica 1'),
    (4, 11,  6, 'Aula 203'),
    (4, 11,  8, 'Aula 204'),
    (4, 11, 10, 'Lab Informatica 4'),
    (4, 12,  7, 'Lab Informatica 3'),
    (4, 12, 10, 'Lab Informatica 4'),
    (4, 13,  3, 'Lab Fisica'),
    (4, 13,  5, 'Lab Informatica 2'),
    (4, 14,  1, 'Aula 101'),
    (4, 14,  2, 'Aula 102'),
    (4, 14,  4, 'Lab Informatica 1'),
    (4, 15,  8, 'Aula 204'),
    (4, 15, 10, 'Lab Informatica 4')
ON CONFLICT (id_school_term, id_enrolled, id_course) DO NOTHING;

-- 3. Realign identity sequences so the API can keep inserting ----------------

SELECT setval(pg_get_serial_sequence('student',     'id_student'),     (SELECT MAX(id_student)     FROM student));
SELECT setval(pg_get_serial_sequence('course',      'id_course'),      (SELECT MAX(id_course)      FROM course));
SELECT setval(pg_get_serial_sequence('school_term', 'id_school_term'), (SELECT MAX(id_school_term) FROM school_term));
SELECT setval(pg_get_serial_sequence('enroll',      'id_enrolled'),    (SELECT MAX(id_enrolled)    FROM enroll));

COMMIT;
