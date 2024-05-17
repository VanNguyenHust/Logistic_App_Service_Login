package com.hust.globalict.main.modules;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QToken is a Querydsl query type for Token
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QToken extends EntityPathBase<Token> {

    private static final long serialVersionUID = -918280953L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QToken token1 = new QToken("token1");

    public final DateTimePath<java.time.LocalDateTime> expirationDate = createDateTime("expirationDate", java.time.LocalDateTime.class);

    public final BooleanPath expired = createBoolean("expired");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isMobile = createBoolean("isMobile");

    public final DateTimePath<java.time.LocalDateTime> refreshExpirationDate = createDateTime("refreshExpirationDate", java.time.LocalDateTime.class);

    public final StringPath refreshToken = createString("refreshToken");

    public final BooleanPath revoked = createBoolean("revoked");

    public final StringPath token = createString("token");

    public final StringPath tokenType = createString("tokenType");

    public final QUserLogin userLogin;

    public QToken(String variable) {
        this(Token.class, forVariable(variable), INITS);
    }

    public QToken(Path<? extends Token> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QToken(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QToken(PathMetadata metadata, PathInits inits) {
        this(Token.class, metadata, inits);
    }

    public QToken(Class<? extends Token> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userLogin = inits.isInitialized("userLogin") ? new QUserLogin(forProperty("userLogin"), inits.get("userLogin")) : null;
    }

}

