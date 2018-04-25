package demo.spring.boot.demospringboot.sdxd.common.api.common.web.doc;

import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedField;

import springfox.documentation.schema.property.field.FieldProvider;

import static com.google.common.collect.Lists.newArrayList;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.doc
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 16/11/2     melvin                 Created
 */
public class RestFieldProvider extends FieldProvider {

    private TypeResolver typeResolver;

    public RestFieldProvider(TypeResolver typeResolver) {
        super(typeResolver);

        this.typeResolver = typeResolver;
    }

    @Override
    public Iterable<ResolvedField> in(ResolvedType resolvedType) {
        MemberResolver memberResolver = new MemberResolver(typeResolver);
        memberResolver.setFieldFilter(new RestFieldFilter());
        if (resolvedType.getErasedType() == Object.class) {
            return newArrayList();
        }
        ResolvedTypeWithMembers resolvedMemberWithMembers = memberResolver.resolve(resolvedType, null, null);
        return newArrayList(resolvedMemberWithMembers.getMemberFields());
    }
}
