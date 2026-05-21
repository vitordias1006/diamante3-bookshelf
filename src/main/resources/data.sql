-- Autores
INSERT INTO autores (id, nome, nacionalidade, bio, data_nascimento) VALUES
(1, 'Machado de Assis', 'Brasileiro', 'Considerado o maior nome da literatura brasileira, fundador da Academia Brasileira de Letras.', '1839-06-21'),
(2, 'Clarice Lispector', 'Brasileira', 'Uma das escritoras mais importantes do modernismo brasileiro, conhecida pelo fluxo de consciência.', '1920-12-10'),
(3, 'George Orwell', 'Britânico', 'Jornalista e escritor inglês, famoso por suas críticas ao totalitarismo.', '1903-06-25'),
(4, 'Gabriel García Márquez', 'Colombiano', 'Ganhador do Nobel de Literatura, pai do Realismo Mágico.', '1927-03-06');

-- Livros
INSERT INTO livros (id, titulo, isbn, ano_publicacao, genero, paginas, sinopse, autor_id) VALUES
(1, 'Dom Casmurro', '978-85-359-0277-5', 1899, 'Romance', 256, 'Bentinho, o Dom Casmurro, narra sua história de amor com Capitu e a suspeita de traição que o perseguiu.', 1),
(2, 'Memórias Póstumas de Brás Cubas', '978-85-359-0278-2', 1881, 'Romance', 224, 'Narrado por um defunto autor, é uma sátira brilhante da sociedade brasileira do século XIX.', 1),
(3, 'A Hora da Estrela', '978-85-359-0279-9', 1977, 'Romance', 96, 'A história de Macabéa, uma nordestina ingênua e solitária vivendo no Rio de Janeiro.', 2),
(4, 'A Paixão Segundo G.H.', '978-85-359-0280-5', 1964, 'Romance', 179, 'Uma mulher defronta-se com uma barata e entra em colapso existencial.', 2),
(5, '1984', '978-0-452-28423-4', 1949, 'Ficção Científica', 328, 'Num futuro distópico, Winston Smith luta contra o regime totalitário do Grande Irmão.', 3),
(6, 'A Revolução dos Bichos', '978-0-452-28424-1', 1945, 'Fábula', 112, 'Alegoria política sobre os perigos do totalitarismo contada por animais de uma fazenda.', 3),
(7, 'Cem Anos de Solidão', '978-0-06-088328-7', 1967, 'Realismo Mágico', 448, 'A saga épica da família Buendía ao longo de sete gerações na cidade fictícia de Macondo.', 4);

-- Resenhas
INSERT INTO resenhas (id, titulo, conteudo, nota, tem_spoiler, livro_id) VALUES
(1, 'Uma obra-prima atemporal', 'Dom Casmurro é, sem dúvida, um dos maiores romances da língua portuguesa. A dúvida sobre a culpa de Capitu é uma das maiores armadilhas literárias já criadas.', 5, true, 1),
(2, 'Machado em seu melhor', 'Memórias Póstumas surpreende pela ousadia narrativa. Um defunto como narrador e ainda assim a obra mais viva da literatura brasileira.', 5, false, 2),
(3, 'Clarice em sua essência', 'A Hora da Estrela é devastador. Macabéa é todas nós, invisíveis ao mundo.', 5, false, 3),
(4, 'Distopia perfeita', '1984 é profético e assustador. Orwell viu o futuro com clareza perturbadora.', 5, false, 5),
(5, 'Genialidade pura', 'Cem Anos de Solidão é uma experiência única. García Márquez criou um universo inteiro em um livro.', 5, false, 7);
