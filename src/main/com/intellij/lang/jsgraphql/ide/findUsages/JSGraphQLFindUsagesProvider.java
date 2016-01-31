/**
 * Copyright (c) 2015, Jim Kynde Meyer
 * All rights reserved.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */
package com.intellij.lang.jsgraphql.ide.findUsages;

import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.lang.jsgraphql.psi.JSGraphQLNamedPropertyPsiElement;
import com.intellij.lang.jsgraphql.psi.JSGraphQLNamedPsiElement;
import com.intellij.lang.jsgraphql.psi.JSGraphQLNamedTypePsiElement;
import com.intellij.lang.jsgraphql.schema.psi.JSGraphQLSchemaFile;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Find usages for named GraphQL and GraphQL Schema PSI elements
 * @see JSGraphQLNamedPsiElement
 */
public class JSGraphQLFindUsagesProvider implements FindUsagesProvider {

    @Nullable
    @Override
    public WordsScanner getWordsScanner() {
        return null;
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof JSGraphQLNamedPsiElement && psiElement.isValid();
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        return "reference.dialogs.findUsages.other";
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        if(element instanceof JSGraphQLNamedPropertyPsiElement) {
            return ((JSGraphQLNamedPropertyPsiElement) element).isScalar() ? "scalar field" : "field";
        }
        if(element instanceof JSGraphQLNamedTypePsiElement) {
            final boolean atom = element.getContainingFile() instanceof JSGraphQLSchemaFile || ((JSGraphQLNamedTypePsiElement) element).isAtom();
            return atom ? "type" : "definition";
        }
        return "unknown";
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        if (element.getParent() instanceof PsiNamedElement) {
            return StringUtil.notNullize(((PsiNamedElement)element.getParent()).getName());
        }
        return "";
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        final String name = element.getParent() instanceof PsiNamedElement ? ((PsiNamedElement)element.getParent()).getName() : null;
        return name != null ? name : element.getText();
    }
}
