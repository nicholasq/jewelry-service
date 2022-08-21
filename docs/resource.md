### Resources

#### Contact

```
Contact {
  id: string
  firstName: string
  lastName: string
  dateOfBirth: string
  email: string
  phone: string
  address: string
  company: string
  jobTitle: string
  notes: string
}
```

#### Pipeline

```
Pipeline {
    id: string
    creation_date: string
    last_update_date: string
    name: string
    description: string
}
```

#### Phase

```
Phase {
    id: string
    pipeline_id: string
    creation_date: string
    last_update_date: string
    name: string
    description: string
}
```

#### Job

```
Job {
    id: string
    phase_id: string
    creation_date: string
    last_update_date: string
    name: string
    description: string
    priority: string
    order: number
    notes: string
}
```

### Resource Summary

```
ResourceSummary {
  id: string
}
```
