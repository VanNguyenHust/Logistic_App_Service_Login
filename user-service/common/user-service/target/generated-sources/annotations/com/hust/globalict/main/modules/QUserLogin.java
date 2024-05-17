package com.hust.globalict.main.modules;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserLogin is a Querydsl query type for UserLogin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserLogin extends EntityPathBase<UserLogin> {

    private static final long serialVersionUID = -1678512532L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserLogin userLogin = new QUserLogin("userLogin");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath loginType = createString("loginType");

    public final StringPath password = createString("password");

    public final QRole role;

    public final SetPath<Token, QToken> tokens = this.<Token, QToken>createSet("tokens", Token.class, QToken.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUser user;

    public final StringPath username = createString("username");

    public QUserLogin(String variable) {
        this(UserLogin.class, forVariable(variable), INITS);
    }

    public QUserLogin(Path<? extends UserLogin> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserLogin(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserLogin(PathMetadata metadata, PathInits inits) {
        this(UserLogin.class, metadata, inits);
    }

    public QUserLogin(Class<? extends UserLogin> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.role = inits.isInitialized("role") ? new QRole(forProperty("role")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

