INSERT INTO student (grade, name, school)
VALUES 
("1st", "Suzie Smith", "Riverside Elementary"),
("Kindergarten", "Johnny Appleseed", "Forest Elementary"),
("6th", "Jimmy Vegas", "Apache Elementary");

INSERT INTO device (device_type_id, asset_tag, serial_number, status)
VALUES 
(1, "12345", "QWE345", "Available"),
(2, "12346", "QWE346", "In Use"),
(3, "12347", "QWE347", "Available");

INSERT INTO student_device (device_id, student_id)
VALUES (1, 2);