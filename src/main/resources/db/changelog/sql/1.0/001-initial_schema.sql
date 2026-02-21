CREATE TABLE IF NOT EXISTS item
(
    id BIGSERIAL,
    active BOOLEAN NOT NULL,
    created_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    created_by BIGINT,
    updated_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
    updated_by BIGINT,
    brand VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    discount_percent DOUBLE PRECISION,
    discount_price DOUBLE PRECISION,
    final_price DOUBLE PRECISION NOT NULL,
    image_urls VARCHAR(255)[] NOT NULL,
    name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    size VARCHAR(255) NOT NULL,
    sub_category VARCHAR(255) NOT NULL,
    uuid VARCHAR(255) NOT NULL,

    CONSTRAINT item_pkey PRIMARY KEY (id),
    CONSTRAINT uk_item_uuid UNIQUE (uuid),
    CONSTRAINT item_category_check CHECK (
        category = ANY (ARRAY['MEN','WOMEN','CHILDREN'])
    )
);