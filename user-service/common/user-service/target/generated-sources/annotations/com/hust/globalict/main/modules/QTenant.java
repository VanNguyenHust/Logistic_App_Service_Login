package com.hust.globalict.main.modules;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTenant is a Querydsl query type for Tenant
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTenant extends EntityPathBase<Tenant> {

    private static final long serialVersionUID = 1588911964L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTenant tenant = new QTenant("tenant");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath code = createString("code");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath status = createString("status");

    public final StringPath translateName = createString("translateName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUser user;

    public QTenant(String variable) {
        this(Tenant.class, forVariable(variable), INITS);
    }

    public QTenant(Path<? extends Tenant> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTenant(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTenant(PathMetadata metadata, PathInits inits) {
        this(Tenant.class, metadata, inits);
    }

    public QTenant(Class<? extends Tenant> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

