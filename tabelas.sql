CREATE TABLE IF NOT EXISTS games (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(150) NOT NULL,
    description TEXT,
    price NUMERIC(10, 2) NOT NULL,
    product_type VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_product_price CHECK (price >= 0),
    CONSTRAINT chk_product_type CHECK (
        product_type IN ('DIGITAL', 'PHYSICAL')
    )
);

CREATE TABLE IF NOT EXISTS platforms (
	id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO platforms (name) VALUES
('PC'),
('PlayStation 4'),
('PlayStation 5'),
('Xbox One'),
('Xbox Series X/S'),
('Nintendo Switch');


CREATE TABLE IF NOT EXISTS game_platforms (
    game_id UUID NOT NULL,
    platform_id UUID NOT NULL,

    PRIMARY KEY (game_id, platform_id),

    CONSTRAINT fk_game
        FOREIGN KEY (game_id)
        REFERENCES games(id),

    CONSTRAINT fk_platform
        FOREIGN KEY (platform_id)
        REFERENCES platforms(id)
);

INSERT INTO games (name, description, price, product_type)
VALUES (
    'Elden Ring',
    'RPG de ação em mundo aberto desenvolvido pela FromSoftware.',
    199.90,
    'DIGITAL'
)
RETURNING *;

INSERT INTO games (
    name,
    description,
    price,
    product_type,
    stock_quantity
)
VALUES (
    'God of War Ragnarök',
    'Jogo de ação e aventura para PlayStation 5.',
    249.90,
    'PHYSICAL',
    10
)
RETURNING *;


INSERT INTO game_platforms (game_id, platform_id)
SELECT g.id, p.id
FROM games g
JOIN platforms p ON
    (g.name = 'Elden Ring'
        AND p.name IN ('PC', 'PlayStation 5', 'Xbox Series X/S'))
    OR
    (g.name = 'God of War Ragnarök'
        AND p.name = 'PlayStation 5')
ON CONFLICT DO NOTHING;



CREATE TABLE IF NOT EXISTS categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL UNIQUE
);

SELECT
    g.name AS game,
    p.name AS platform
FROM game_platforms gp
INNER JOIN games g ON g.id = gp.game_id
INNER JOIN platforms p ON p.id = gp.platform_id
ORDER BY g.name, p.name;

INSERT INTO categories (name) VALUES
('RPG'),
('Action'),
('Adventure'),
('Sports'),
('Racing'),
('Strategy'),
('Horror')
ON CONFLICT (name) DO NOTHING;

ALTER TABLE games
ADD COLUMN IF NOT EXISTS category_id UUID;

ALTER TABLE games
ADD CONSTRAINT fk_game_category
FOREIGN KEY (category_id)
REFERENCES categories(id);

UPDATE games
SET category_id = (
    SELECT id FROM categories
    WHERE name = 'RPG'
)
WHERE name = 'Elden Ring';

UPDATE games
SET category_id = (
    SELECT id FROM categories
    WHERE name = 'Action'
)
WHERE name = 'God of War Ragnarök';

SELECT
    g.name AS game,
    c.name AS category
FROM games g
INNER JOIN categories c
    ON g.category_id = c.id;

SELECT
    g.id,
    g.name,
    g.price,
    g.product_type,
    g.stock_quantity,
    c.name AS category,
    STRING_AGG(p.name, ', ') AS platforms
FROM games g
LEFT JOIN categories c ON c.id = g.category_id
LEFT JOIN game_platforms gp ON gp.game_id = g.id
LEFT JOIN platforms p ON p.id = gp.platform_id
GROUP BY
    g.id,
    g.name,
    g.price,
    g.product_type,
    g.stock_quantity,
    c.name
ORDER BY g.name;