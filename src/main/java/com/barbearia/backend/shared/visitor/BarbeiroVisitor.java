package com.barbearia.backend.shared.visitor;

import com.barbearia.backend.core.entities.Barbeiro;
import java.util.List;

public interface BarbeiroVisitor<T> {
    T visit(Barbeiro barbeiro);
    T visitAll(List<Barbeiro> barbeiros);
}