package net.shopxx.dao;

import net.shopxx.entity.Sn;

public abstract interface SnDao
{
  public abstract String generate(Sn.Type paramType);
}